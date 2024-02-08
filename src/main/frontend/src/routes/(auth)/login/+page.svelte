<script lang="ts">
    import Card from "$lib/components/Card.svelte";
    import Input from "$lib/components/Input.svelte";
    import Checkbox from "$lib/components/Checkbox.svelte";
    import Button from "$lib/components/Button.svelte";
    import {type AuthenticationRequest, login} from "$lib/api/auth";
    import Cookies from "js-cookie";
    import type {ApiError} from "$lib/api/main";

    let request: AuthenticationRequest = {
        email: "",
        password: "",
    }

    let errors: ApiError;

    async function onSubmit() {
        await login(request).then(response => {
            Cookies.set('Authorization', `Bearer ${response.access_token}`, {expires: 1, sameSite: 'lax'});
            Cookies.set('refreshToken', response.refresh_token, {expires: 7, sameSite: 'lax'});

            window.location.replace('/items');
        }).catch(error => {
            errors = error;
        });
    }

</script>

<svelte:head>
    <title>Login - EM</title>
</svelte:head>

<Card
        title="Log In">
    <span slot="content">

        {#if errors && errors.message != null }
            <p class="text-red-500">{errors.message}</p>
        {/if}

        <form on:submit|preventDefault={onSubmit}>
            <Input
                    type="email"
                    label="Email"
                    name="email"
                    placeholder="email"
                    required
                    autofocus
                    autocomplete="username"
                    bind:value={request.email}
            />

            <Input
                    type="password"
                    label="Password"
                    name="password"
                    placeholder="password"
                    required
                    autocomplete="current-password"
                    bind:value={request.password}
            />

<!--            <Checkbox name="remember" label="Remember me"/>-->

            <div class="flex items-center justify-end mt-4">

                    <a href="/resetPassword" class="link link-hover">
                        Reset password
                    </a>

                    <Button class="ml-4" type="submit" disabled={false}>
                        LOG IN
                    </Button>
            </div>
        </form>

    </span>

</Card>